document.addEventListener('DOMContentLoaded', function() {
    fetchAvailableCars();
});

function fetchAvailableCars() {
    const contextPath = window.location.pathname.split('/')[1];
    fetch(`/${contextPath}/api/cars`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(cars => {
            displayCars(cars);
        })
        .catch(error => {
            console.error('Error fetching cars:', error);
        });
}

function displayCars(cars) {
    const carsContainer = document.getElementById('cars-container');
    carsContainer.innerHTML = '';

    cars.forEach(car => {
        const carCard = document.createElement('div');
        carCard.className = 'car-card';

        carCard.innerHTML = `
            
            <div class="car-info">
                <h3>${car.make} ${car.model} (${car.year})</h3>
                <p>Color: ${car.color}</p>
                <p class="price">R$${car.pricePerDay.toFixed(2)} per day</p>
                <p>Status: ${car.available ? 'Available' : 'Rented'}</p>
            </div>
        `;

        carsContainer.appendChild(carCard);
    });
}