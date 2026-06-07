@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');

/* Aplicando a fonte moderna */
body, 
.login-pf body,
h1, h2, input, button, label {
    font-family: 'Inter', sans-serif !important;
}

.login-pf body {
    background: linear-gradient(135deg, #06142e 0%, #0f3570 50%, #4287f5 100%) !important;
    background-size: cover !important;
    background-attachment: fixed !important;
    background-color: #06142e;
}

/* Ajuste da posição do Título para ficar mais próximo ao card */
#kc-header {
    margin-top: 6vh; /* Top distance */
    margin-bottom: 20px !important; /* Login card distance */
    padding: 0;
}

#kc-header-wrapper {
    visibility: hidden;
    font-size: 0; /* Remove space occupied by the original text */
}

#kc-header-wrapper::after {
    content: "Url Shortener";
    visibility: visible;
    display: block;
    font-weight: 700;
    font-size: 32px;
    letter-spacing: -0.5px;
    color: #ffffff;
    text-align: center;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.login-pf-page .card-pf {
    border-radius: 16px;
    padding: 35px;
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.4);
    border-top: none;
    background-color: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(5px);
}

.form-control {
    border-radius: 8px !important;
    border: 1px solid #d1d5db;
    padding: 10px 14px;
    height: auto;
}

.form-control:focus {
    border-color: #4287f5;
    box-shadow: 0 0 0 3px rgba(66, 135, 245, 0.2);
}

.btn-primary {
    background-color: #4287f5 !important;
    background-image: none !important;
    border: none !important;
    border-radius: 8px !important;
    padding: 12px;
    font-weight: 600;
    font-size: 15px;
    transition: background-color 0.2s ease;
}

.btn-primary:hover {
    background-color: #0f3570 !important;
}

a {
    color: #4287f5;
    transition: color 0.2s ease;
}

a:hover {
    color: #0f3570;
}