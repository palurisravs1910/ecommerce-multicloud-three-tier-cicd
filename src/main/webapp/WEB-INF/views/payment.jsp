<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<c:set var="pageTitle" value="Payment - ShopEasy" />
<%@ include file="common/header.jsp" %>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-header bg-success text-white text-center py-3">
                    <h4 class="mb-0"><i class="bi bi-shield-lock"></i> Secure Payment</h4>
                    <small>Order #${orderId} &mdash; Total: $<fmt:formatNumber value="${amount}" pattern="#,##0.00"/></small>
                </div>
                <div class="card-body p-4">

                    <div id="payment-message" class="alert d-none"></div>

                    <!-- Stripe Card Element -->
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Card Details</label>
                        <div id="card-element" class="form-control py-3"
                             style="height:auto; border:1px solid #ced4da; border-radius:0.375rem;">
                            <!-- Stripe.js injects the card element here -->
                        </div>
                        <div id="card-errors" class="text-danger small mt-1" role="alert"></div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Name on Card</label>
                        <input type="text" id="cardName" class="form-control"
                               placeholder="John Doe" value="${sessionScope.userName}">
                    </div>

                    <div class="d-grid">
                        <button id="submit-payment" class="btn btn-success btn-lg">
                            <span id="pay-spinner" class="spinner-border spinner-border-sm d-none me-2"></span>
                            <i class="bi bi-lock-fill" id="pay-icon"></i>
                            <span id="pay-text">Pay $<fmt:formatNumber value="${amount}" pattern="#,##0.00"/></span>
                        </button>
                    </div>

                    <p class="text-center text-muted small mt-3">
                        <i class="bi bi-lock"></i> Payments are 256-bit SSL encrypted via Stripe.
                    </p>

                    <!-- Test card hint -->
                    <div class="alert alert-info small mt-3">
                        <strong>Test Card:</strong> 4242 4242 4242 4242 &mdash;
                        Any future expiry &mdash; Any 3-digit CVC
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Stripe.js -->
<script src="https://js.stripe.com/v3/"></script>
<script>
    const stripe       = Stripe('${publishableKey}');
    const elements     = stripe.elements();
    const clientSecret = '${clientSecret}';
    const orderId      = '${orderId}';

    // Card element styling
    const cardStyle = {
        style: {
            base: {
                color: '#32325d',
                fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
                fontSmoothing: 'antialiased',
                fontSize: '16px',
                '::placeholder': { color: '#aab7c4' }
            },
            invalid: { color: '#fa755a', iconColor: '#fa755a' }
        }
    };

    const card = elements.create('card', cardStyle);
    card.mount('#card-element');

    // Real-time validation errors
    card.addEventListener('change', function(event) {
        const displayError = document.getElementById('card-errors');
        displayError.textContent = event.error ? event.error.message : '';
    });

    // Handle payment submission
    document.getElementById('submit-payment').addEventListener('click', async function() {
        setLoading(true);

        const {paymentIntent, error} = await stripe.confirmCardPayment(clientSecret, {
            payment_method: {
                card: card,
                billing_details: {
                    name: document.getElementById('cardName').value
                }
            }
        });

        if (error) {
            showMessage(error.message, 'danger');
            setLoading(false);
        } else if (paymentIntent.status === 'succeeded') {
            showMessage('Payment successful! Redirecting...', 'success');
            // Notify server
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/payment';
            addHidden(form, 'orderId', orderId);
            addHidden(form, 'status', 'success');
            document.body.appendChild(form);
            form.submit();
        } else {
            showMessage('Payment failed. Please try again.', 'danger');
            setLoading(false);
        }
    });

    function setLoading(isLoading) {
        document.getElementById('submit-payment').disabled = isLoading;
        document.getElementById('pay-spinner').classList.toggle('d-none', !isLoading);
        document.getElementById('pay-icon').classList.toggle('d-none', isLoading);
    }

    function showMessage(msg, type) {
        const el = document.getElementById('payment-message');
        el.className = 'alert alert-' + type;
        el.textContent = msg;
        el.classList.remove('d-none');
    }

    function addHidden(form, name, value) {
        const input = document.createElement('input');
        input.type  = 'hidden';
        input.name  = name;
        input.value = value;
        form.appendChild(input);
    }
</script>

<%@ include file="common/footer.jsp" %>
