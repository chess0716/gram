from pymongo import MongoClient
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score
from sklearn.preprocessing import LabelEncoder
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.neighbors import KNeighborsClassifier

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
model = RandomForestClassifier(n_estimators=50, max_depth=10, min_samples_leaf=2, random_state=42)
model.fit(X_train, y_train)
# Prepare New Data
new_data = pd.DataFrame({
    'gender': ['남자'],
    'age': ['20대'],
    'environment': ['실내'],
    'frequency': ['주 1-2일'],
    'budget': ['100000원 미만'],
    'time_preference': ['주말'],
    'interests': ["승마, 실내서바이벌, 산악자전거, 아이스링크"]
})
for column in ['gender', 'age', 'environment', 'frequency', 'budget', 'time_preference']:
    new_data[column] = label_encoders[column].transform(new_data[column])

for sport in sports:
    new_data[sport] = new_data['interests'].apply(lambda x: 1 if sport in x else 0)
new_features = new_data[['gender', 'age', 'environment', 'frequency', 'budget', 'time_preference'] + sports]

# Predict New Data with Probability
prob_predictions = model.predict_proba(new_features)
prob_threshold = 0.3  # 예측 확률 임계값 설정
predicted_sports = []

print("Prob_predictions 구조:", [p.shape for p in prob_predictions])

# 각 스포츠별 확률 정보 출력
for i, sport in enumerate(sports):
    print(f"{sport} 예측 확률: {prob_predictions[i]}")

# 정확도 계산
predictions = model.predict(X_test)
accuracy = accuracy_score(y_test, predictions)
print("정확도:", accuracy)

for i, sport in enumerate(sports):
    # 확률 배열에서 '선호함' 클래스의 확률을 추출합니다.
    # 비치 발리볼 같이 예외적으로 확률 배열이 하나만 있는 경우를 처리
    if prob_predictions[i].shape[1] == 1:  # 클래스 확률이 하나만 존재하는 경우
        sport_prob = prob_predictions[i][0][0] if prob_predictions[i][0][0] > 0.5 else 0  # 임계값보다 큰 경우에만 선호한다고 간주
    else:
        sport_prob = prob_predictions[i][0][1]  # 정상적으로 두 클래스의 확률이 있는 경우

    if sport_prob >= prob_threshold:
        predicted_sports.append(sport)

print("확률 임계값에 기반한 선호 스포츠 예측: ", predicted_sports)

from sklearn.metrics import classification_report

# Decision Tree and k-NN Models
decision_tree_model = DecisionTreeClassifier(random_state=42)
knn_model = KNeighborsClassifier()

# Training Decision Tree
decision_tree_model.fit(X_train, y_train)
dt_predictions = decision_tree_model.predict(X_test)
dt_accuracy = accuracy_score(y_test, dt_predictions)
print("Decision Tree Accuracy:", dt_accuracy)
print(classification_report(y_test, dt_predictions, target_names=sports))

# Training k-NN
knn_model.fit(X_train, y_train)
knn_predictions = knn_model.predict(X_test)
knn_accuracy = accuracy_score(y_test, knn_predictions)
print("k-NN Accuracy:", knn_accuracy)
print(classification_report(y_test, knn_predictions, target_names=sports))
