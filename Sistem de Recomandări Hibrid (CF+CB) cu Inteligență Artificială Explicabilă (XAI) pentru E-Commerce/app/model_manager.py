import joblib
from pathlib import Path

MODEL_DIR = Path(__file__).resolve().parents[1] / "models"
MODEL_DIR.mkdir(exist_ok=True)

def save_model(obj, name):
    joblib.dump(obj, MODEL_DIR / f"{name}.pkl")

def load_model(name):
    return joblib.load(MODEL_DIR / f"{name}.pkl")
