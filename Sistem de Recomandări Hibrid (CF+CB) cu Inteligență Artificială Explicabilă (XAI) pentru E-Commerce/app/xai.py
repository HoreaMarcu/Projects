import shap
from lime.lime_text import LimeTextExplainer

def explain_with_shap(model_predict_fn, background_data, instance):
    explainer = shap.KernelExplainer(model_predict_fn, background_data)
    shap_values = explainer.shap_values(instance)
    return shap_values

def explain_text_with_lime(text, predict_proba_fn, class_names=None):
    explainer = LimeTextExplainer(class_names=class_names)
    exp = explainer.explain_instance(text_instance=text, classifier_fn=predict_proba_fn, num_features=6)
    return exp.as_list()
