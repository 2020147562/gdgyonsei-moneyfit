import pandas as pd
from sklearn.ensemble import IsolationForest
import os
from dotenv import load_dotenv
import psycopg2
from psycopg2.extras import RealDictCursor
from flask import Flask, request, jsonify
import logging

# Flask 애플리케이션 초기화
app = Flask(__name__)

# 로깅 설정
logging.basicConfig(level=logging.DEBUG, format='%(asctime)s %(levelname)s %(message)s')

# .env 파일 로드
load_dotenv()

# 데이터베이스 연결 정보 가져오기
db_config = {
    "dbname": os.getenv("DB_NAME"),
    "user": os.getenv("DB_USERNAME"),
    "password": os.getenv("DB_PASSWORD"),
    "host": os.getenv("DB_HOST", "db"),
    "port": os.getenv("DB_PORT")
}

# 카테고리별 모델 저장
models = {}

# postgre 데이터 가져오는 함수
def get_last_month_expenses(item_id, user_email):
    """
    항목 ID와 사용자 이메일을 기반으로 지난 한 달 소비 기록을 가져옵니다.
    :param item_id: 항목 ID
    :param user_email: 사용자 이메일
    :return: 사용자의 저번달 소비 기록 딕셔너리 리스트
    """
    try:
        logging.debug(f"item_id: {item_id}, user_email: {user_email}")
        # 데이터베이스 연결
        connection = psycopg2.connect(**db_config)
        cursor = connection.cursor(cursor_factory=RealDictCursor)  # 결과를 딕셔너리로 반환
        logging.debug("Database connection established.")

        # 저번 달 소비 기록 조회
        query = """
        SELECT expense_amount, upper_category_type
        FROM spending
        WHERE member_email = %s
          AND date_time >= DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month')
          AND date_time < DATE_TRUNC('month', CURRENT_DATE);
        """
        cursor.execute(query, (user_email,))  # 사용자 이메일을 바인딩하여 쿼리 실행
        rows = cursor.fetchall()
        logging.debug("Query executed successfully.")

        # 결과 반환
        return rows

    except Exception as e:
        logging.error(f"Error Detected: {e}")
        return []

    finally:
        # 연결 종료
        if 'cursor' in locals() and cursor:
            cursor.close()
        if 'connection' in locals() and connection:
            connection.close()

@app.route('/update_model', methods=['POST'])
def update_model():
    """
    항목 ID와 사용자 이메일을 기반으로 지난 한 달 사용 기록을 가져와 모델을 학습
    소비 기록 딕셔너리를 사용하여 카테고리별 IsolationForest 모델을 학습합니다.
    :param expenses_dict: 소비 기록 딕셔너리 리스트
    :return: 카테고리별 IsolationForest 모델 딕셔너리
    """
    data = request.json
    item_id = data.get("item_id")
    user_email = data.get("user_email")

    if not item_id or not user_email:
        return jsonify({"error": "item_id or user_email are necessary."}), 400

    # 지난 한 달 소비 기록 가져오기
    expenses = get_last_month_expenses(item_id, user_email)

    if not expenses:
        return jsonify({"error": "There is no spending record. 1"}), 400

    # DataFrame 생성 및 모델 학습
    df = pd.DataFrame(expenses)
    if df.empty:
        return jsonify({"error": "There is no spending record. 2"}), 400
    global models
    models = {}

    for category in df['upper_category_type'].unique():
        category_data = df[df['upper_category_type'] == category][['expense_amount']]
        model = IsolationForest(contamination=0.1, random_state=42)
        model.fit(category_data)
        models[category] = model

    return jsonify({"message": "Model Train Completed"}), 200

@app.route('/is_over_consumption', methods=['POST'])
def is_over_consumption():
    """
    항목 ID와 사용자 이메일을 기반으로 전체 항목 정보를 가져와 이상치 여부를 판단
    """
    data = request.json
    item_id = data.get("item_id")
    user_email = data.get("user_email")

    if not item_id or not user_email:
        return jsonify({"error": "item_id or user_email are necessary but omitted"}), 400

    try:
        # 데이터베이스 연결
        connection = psycopg2.connect(**db_config)
        cursor = connection.cursor(cursor_factory=RealDictCursor)
        logging.debug("Database connection established for is_over_consumption.")

        # 특정 항목 정보 가져오기
        query = """
        SELECT expense_amount, upper_category_type
        FROM spending
        WHERE id = %s AND member_email = %s;
        """
        cursor.execute(query, (item_id, user_email))
        record = cursor.fetchone()
        logging.debug("Query executed successfully for is_over_consumption.")

        if not record:
            return jsonify({"error": "There is no spending record. 3"}), 404

        # 모델을 사용하여 이상치 판단
        category = record['upper_category_type']
        if category not in models:
            # 해당 카테고리에 모델이 없는 경우 "정상"으로 간주
            logging.debug(f"No model found for CATEGORY: '{category}'. Marking as normal.")
            return jsonify({
                "item_id": item_id,
                "is_over_consumption": False,  # 정상치로 판단
                "anomaly_score": None
            }), 200

        model = models[category]
        expense_amount = pd.DataFrame([[record['expense_amount']]], columns=['expense_amount'])
        is_outlier = bool(model.predict(expense_amount)[0] == -1)  # -1: 이상치
        anomaly_score = float(model.decision_function(expense_amount)[0])

        return jsonify({
            "item_id": item_id,
            "is_over_consumption": is_outlier,
            "anomaly_score": anomaly_score
        }), 200

    except Exception as e:
        logging.error(f"Error Detected: {e}")
        return jsonify({"error": "Internal Server Error Occurred"}), 500

    finally:
        if 'cursor' in locals() and cursor:
            cursor.close()
        if 'connection' in locals() and connection:
            connection.close()

# Flask 애플리케이션 실행
if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000, debug=True)
