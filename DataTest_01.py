from pymongo import MongoClient
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score
from sklearn.preprocessing import LabelEncoder

# MongoDB Connection
client = MongoClient('mongodb://localhost:27017/')
db = client['gram_database']
collection = db['gram_collection']

# Load Data
data = list(collection.find())
df = pd.DataFrame(data)

# Rename Columns
df.rename(columns={
    '타임스탬프': 'timestamp',
    '성별': 'gender',
    '나이': 'age',
    '선호 장소': 'environment',
    '주간 운동량': 'frequency',
    '운동 가용 금액': 'budget',
    '운동 시간': 'time_preference',
    '선호 종목(복수선택 가능)': 'interests'
}, inplace=True)

# Sports Column Transformation
sports = ['아이스링크', '볼더링', '양궁', '사격', '스쿼시', '플라잉요가', '바둑', '체스', '홀덤',
          '트릭킹', '폴댄스', '실내서바이벌', '스쿠버다이빙', '프리다이빙', '스노클링', '비치 발리볼',
          '서핑', '게이트볼', '승마', '패러글라이딩', '카누', '카약', '산악자전거', '스케이트보드']

for sport in sports:
    df[sport] = df['interests'].apply(lambda x: 1 if sport in x else 0)

# Setup Label Encoders
label_encoders = {}
for column in ['gender', 'age', 'environment', 'frequency', 'budget', 'time_preference']:
    le = LabelEncoder()
    df[column] = le.fit_transform(df[column])
    label_encoders[column] = le  # Store encoders

# Feature Selection
features = df[['gender', 'age', 'environment', 'frequency', 'budget', 'time_preference'] + sports]
labels = df[sports]

# Data Split
X_train, X_test, y_train, y_test = train_test_split(features, labels, test_size=0.2, random_state=42)

# RandomForest Model
model = RandomForestClassifier(n_estimators=50, max_depth=15, min_samples_leaf=1, random_state=42)
model.fit(X_train, y_train)

# Prepare New Data
new_data_1 = pd.DataFrame({
    'timestamp': ['2024. 5. 2 오후 12:06:02'],
    'gender': ['여자'],
    'age': ['20대'],
    'environment': ['실내'],
    'frequency': ['주 1-2일'],
    'budget': ['100000원 미만'],
    'time_preference': ['주말'],
    'interests': ["승마, 실내서바이벌, 산악자전거, 아이스링크"]
})

# 레이블 인코딩 적용
for column in ['gender', 'age', 'environment', 'frequency', 'budget', 'time_preference']:
    new_data_1[column] = label_encoders[column].transform(new_data_1[column])

for sport in sports:
    new_data_1[sport] = new_data_1['interests'].apply(lambda x: 1 if sport in x else 0)

new_features_1 = new_data_1[['gender', 'age', 'environment', 'frequency', 'budget', 'time_preference'] + sports]

# Predict New Data 1 with Probability
prob_predictions_1 = model.predict_proba(new_features_1)
predicted_sports_1 = []

for i, sport in enumerate(sports):
    # 확률 배열에서 '선호함' 클래스의 확률을 추출
    if prob_predictions_1[i].shape[1] == 1:
        sport_prob = prob_predictions_1[i][0][0] if prob_predictions_1[i][0][0] > 0.5 else 0
    else:
        sport_prob = prob_predictions_1[i][0][1]

    if sport_prob >= 0.3:  # 확률 임계값 설정
        predicted_sports_1.append(sport)

print("새로운 데이터 1에 대한 선호 스포츠 예측: ", predicted_sports_1)

# 추가할 새로운 데이터 2
new_data_2 = pd.DataFrame({
    'timestamp': ['2024. 5. 2 오전 11:41:45'],
    'gender': ['여자'],
    'age': ['30대'],
    'environment': ['실외'],
    'frequency': ['주 1-2일'],
    'budget': ['100000원 미만'],
    'time_preference': ['주말'],
    'interests': ["승마, 패러글라이딩, 스쿠버다이빙, 스쿼시,사격,실내서바이벌,트릭킹,폴댄스"]
})

# 레이블 인코딩 적용
for column in ['gender', 'age', 'environment', 'frequency', 'budget', 'time_preference']:
    new_data_2[column] = label_encoders[column].transform(new_data_2[column])

for sport in sports:
    new_data_2[sport] = new_data_2['interests'].apply(lambda x: 1 if sport in x else 0)

new_features_2 = new_data_2[['gender', 'age', 'environment', 'frequency', 'budget', 'time_preference'] + sports]

# Predict New Data 2 with Probability
prob_predictions_2 = model.predict_proba(new_features_2)
predicted_sports_2 = []

for i, sport in enumerate(sports):
    # 확률 배열에서 '선호함' 클래스의 확률을 추출
    if prob_predictions_2[i].shape[1] == 1:
        sport_prob = prob_predictions_2[i][0][0] if prob_predictions_2[i][0][0] > 0.5 else 0
    else:
        sport_prob = prob_predictions_2[i][0][1]

    if sport_prob >= 0.3:  # 확률 임계값 설정
        predicted_sports_2.append(sport)

print("새로운 데이터 2에 대한 선호 스포츠 예측: ", predicted_sports_2)

# 추가할 새로운 데이터 3
new_data_3 = pd.DataFrame({
    'timestamp': ['2024. 5. 2 오후 02:30:00'],
    'gender': ['여자'],
    'age': ['30대'],
    'environment': ['실외'],
    'frequency': ['주 1-2일'],
    'budget': ['100000원 미만'],
    'time_preference': ['주말'],
    'interests': ["서핑, 카누, 카약"]
})

# 레이블 인코딩 적용
for column in ['gender', 'age', 'environment', 'frequency', 'budget', 'time_preference']:
    new_data_3[column] = label_encoders[column].transform(new_data_3[column])

for sport in sports:
    new_data_3[sport] = new_data_3['interests'].apply(lambda x: 1 if sport in x else 0)

new_features_3 = new_data_3[['gender', 'age', 'environment', 'frequency', 'budget', 'time_preference'] + sports]

# Predict New Data 3 with Probability
prob_predictions_3 = model.predict_proba(new_features_3)
predicted_sports_3 = []

for i, sport in enumerate(sports):
    # 확률 배열에서 '선호함' 클래스의 확률을 추출
    if prob_predictions_3[i].shape[1] == 1:
        sport_prob = prob_predictions_3[i][0][0] if prob_predictions_3[i][0][0] > 0.5 else 0
    else:
        sport_prob = prob_predictions_3[i][0][1]

    if sport_prob >= 0.3:  # 확률 임계값 설정
        predicted_sports_3.append(sport)

print("새로운 데이터 3에 대한 선호 스포츠 예측: ", predicted_sports_3)

# 테스트 데이터를 사용하여 모델 평가
predictions = model.predict(X_test)
accuracy = accuracy_score(y_test, predictions)
print("테스트 정확도:", accuracy)
