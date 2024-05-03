from pymongo import MongoClient
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.compose import ColumnTransformer
from sklearn.impute import SimpleImputer
import shap

# MongoDB 연결
client = MongoClient('mongodb://localhost:27017/')
db = client['gram_database']
collection = db['gram_collection']

# 데이터 로드
data = list(collection.find())
df = pd.DataFrame(data)

# ObjectId 제거
df.drop(columns=['_id'], inplace=True)

# 데이터 전처리: 열 이름 변경 및 필요한 데이터 타입 변환
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

# '나이'와 '운동 가용 금액' 변환
df['age'] = df['age'].str.extract('(\d+)').astype(int)
df['budget'] = df['budget'].replace({'무료': '0', '원 미만': '', '원 초과': '', '원 이상': '', ',': ''}, regex=True).astype(int)

# 범주형 데이터 인코딩 및 수치형 데이터 스케일링
categorical_features = ['gender', 'environment', 'frequency', 'time_preference']
numeric_features = ['age', 'budget']

# 전처리 파이프라인 설정
preprocessor = ColumnTransformer(
    transformers=[
        ('num', Pipeline(steps=[('imputer', SimpleImputer(strategy='median')), ('scaler', StandardScaler())]), numeric_features),
        ('cat', Pipeline(steps=[('imputer', SimpleImputer(strategy='most_frequent')), ('onehot', OneHotEncoder(handle_unknown='ignore'))]), categorical_features)
    ])

# 모델 파이프라인
pipeline = Pipeline(steps=[
    ('preprocessor', preprocessor),
    ('classifier', RandomForestClassifier(n_estimators=100, random_state=42))
])

# 종속 변수 설정 및 데이터 분할
# 이 부분은 프로젝트 요구사항에 따라 조정할 필요가 있습니다. 예를 들어, 'interests'를 다른 방식으로 처리할 수 있습니다.
X = df.drop(['timestamp', 'interests'], axis=1)
y = df['interests'].apply(lambda x: x[0] if isinstance(x, list) and x else 'Unknown')  # 리스트의 첫 요소를 사용하거나 기본값 설정
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 모델 학습
pipeline.fit(X_train, y_train)

# 모델 평가
print("Model Accuracy:", pipeline.score(X_test, y_test))


