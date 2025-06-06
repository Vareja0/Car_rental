document.addEventListener('DOMContentLoaded', function() {
    fetchAvailableCars();
    setupFormValidation();
});

function fetchAvailableCars() {
    const contextPath = window.location.pathname.split('/')[1];
    fetch(`/${contextPath}/api/cars`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.status);
            }
            return response.json();
        })
        .then(cars => {
            console.log(cars)
            populateCarSelect(cars);
        })
        .catch(error => {
            console.error('Error fetching cars:', error);
        });
}

function populateCarSelect(cars) {
    const carSelect = document.getElementById('carSelect');


    while (carSelect.options.length > 1) {
        carSelect.remove(1);
    }


    cars.filter(car => car.available).forEach(car => {
        const option = document.createElement('option');
        option.value = car.id;
        option.textContent = `${car.make} ${car.model} ($${car.pricePerDay.toFixed(2)}/day)`;
        carSelect.appendChild(option);
    });
}

function setupFormValidation() {
    const bookingForm = document.getElementById('bookingForm');
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');


    const today = new Date().toISOString().split('T')[0];
    startDateInput.min = today;
    endDateInput.min = today;


    startDateInput.addEventListener('change', function() {
        endDateInput.min = this.value;
    });

    bookingForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const carId = document.getElementById('carSelect').value;
        const customerName = document.getElementById('customerName').value;
        const customerEmail = document.getElementById('customerEmail').value;
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;

        submitBooking(carId, customerName, customerEmail, startDate, endDate);
    });
}

function submitBooking(carId, customerName, customerEmail, startDate, endDate) {
    const formData = new URLSearchParams();
    formData.append('carId', carId);
    formData.append('customerName', customerName);
    formData.append('customerEmail', customerEmail);
    formData.append('startDate', startDate);
    formData.append('endDate', endDate);

    fetch('/car_rental_war_exploded/api/bookings', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text) });
            }
            return response.json();
        })
        .then(data => {
            showResultMessage(`Booking successful! Total price: $${data.totalPrice.toFixed(2)}`, true);
            // Reset form
            document.getElementById('bookingForm').reset();
            // Refresh car list
            fetchAvailableCars();
        })
        .catch(error => {
            showResultMessage(`Error: ${error.message}`, false);
        });
}

function showResultMessage(message, isSuccess) {
    const resultDiv = document.getElementById('bookingResult');
    resultDiv.textContent = message;
    resultDiv.className = isSuccess ? 'result-message success' : 'result-message error';
}