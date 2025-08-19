function selectLayout(layoutId) {
    // Remove selected class from all cards
    document.querySelectorAll('.layout-card').forEach(card => {
        card.classList.remove('selected');
    });
    
    // Add selected class to clicked card
    document.getElementById('layout-card-' + layoutId).classList.add('selected');
    
    // Set the hidden input value
    document.getElementById('selectedLayoutId').value = layoutId;
    
    // Show the number of seats form
    document.getElementById('otherFields').style.display = 'block';
}

function validateForm() {
    var layoutId = document.getElementById('selectedLayoutId').value;
    if (!layoutId) {
        alert('Please select a layout first');
        return false;
    }
    return true;
}
// Set up the removal confirmation modal
document.addEventListener('DOMContentLoaded', function() {
    // Handle clicks on removal links
    document.querySelectorAll('.table-remove-btn').forEach(function(button) {
        button.addEventListener('click', function(event) {
            event.preventDefault();

            // Get the URL from the clicked link
            const removeUrl = this.getAttribute('href');

            // Update the confirm button's href
            document.getElementById('confirmRemoveBtn').setAttribute('href', removeUrl);

            // Show the modal (using Bootstrap JavaScript)
            var removeModal = new bootstrap.Modal(document.getElementById('removeTableModal'));
            removeModal.show();
        });
    });
});

//reservation Request js for display
document.addEventListener('DOMContentLoaded', function() {
    const radioButtons = document.querySelectorAll('input[type="radio"][name="seating"]');

    radioButtons.forEach(radio => {
        radio.addEventListener('change', function() {
            // Remove active class and fade out all labels smoothly
            document.querySelectorAll('.btn-outline-primary').forEach(btn => {
                btn.style.transition = 'all 0.3s ease';
                btn.classList.remove('active');
            });

            // Add active class with animation to selected label
            if (this.checked) {
                const selectedBtn = this.closest('.btn');
                selectedBtn.classList.add('active');

                // Add subtle bounce effect
                selectedBtn.style.transform = 'scale(1.05)';
                setTimeout(() => {
                    selectedBtn.style.transform = 'translateY(-2px)';
                }, 150);
            }
        });

        // Set initial active state if pre-selected
        if (radio.checked) {
            radio.closest('.btn').classList.add('active');
        }
    });
});