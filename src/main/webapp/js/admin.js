document.addEventListener('DOMContentLoaded', function () {
    fetchRentalRecords();
});

function fetchRentalRecords() {
    const contextPath = window.location.pathname.split('/')[1];
    fetch(`/${contextPath}/api/admin/rentals`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(rentals => {
            displayRentalRecords(rentals);
        })
        .catch(error => {
            console.error('Error fetching rental records:', error);
        });
}

function displayRentalRecords(rentals) {
    const tableBody = document.querySelector('#rentalsTable tbody');
    tableBody.innerHTML = '';

    if (rentals.length === 0) {
        const row = document.createElement('tr');
        row.innerHTML = '<td colspan="7" style="text-align: center;">No rental records found</td>';
        tableBody.appendChild(row);
        return;
    }

    rentals.forEach(rental => {
        const row = document.createElement('tr');

        row.innerHTML = `
            <td>${rental.id}</td>
            <td>${rental.carId}</td>
            <td>${rental.customerName}</td>
            <td>${rental.customerEmail}</td>
            <td>${new Date(rental.startDate).toLocaleDateString()}</td>
            <td>${new Date(rental.endDate).toLocaleDateString()}</td>
            <td>R$${rental.totalPrice.toFixed(2)}</td>
        `;

        tableBody.appendChild(row);
    });
}