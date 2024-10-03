document.addEventListener('DOMContentLoaded', function() {
    const forms = {
        'depositForm': '/api/banking/deposit',
        'withdrawForm': '/api/banking/withdraw',
        'transferForm': '/api/banking/transfer'
    };

    Object.entries(forms).forEach(([formId, url]) => {
        const form = document.getElementById(formId);
        if (form) {
            form.addEventListener('submit', function(e) {
                e.preventDefault();
                const formData = new FormData(form);
                performBankingOperation(url, formData);
            });
        }
    });

    var toast = document.getElementById('toast');
    if (toast) {
        toast.classList.add('show');
        setTimeout(function() {
            toast.classList.remove('show');
        }, 3000);
    }
});

function performBankingOperation(url, formData) {
    fetch(url, {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(result => {
        showToast(result.message, result.success);
        if (result.success) {
            location.reload();
        }
    })
    .catch(error => {
        showToast("An error occurred. Please try again.", false);
    });
}

function showToast(message, isSuccess) {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = isSuccess ? 'toast success' : 'toast error';
    toast.style.display = 'block';
    setTimeout(() => {
        toast.style.display = 'none';
    }, 3000);
}
