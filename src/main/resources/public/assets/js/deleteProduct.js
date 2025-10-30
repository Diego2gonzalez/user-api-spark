document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('deleteProductForm');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const idValue = document.getElementById('deleteProductId').value.trim();
    const id = parseInt(idValue);

    if (!id || id <= 0) {
      alert('Please enter a valid product ID.');
      return;
    }

    console.log("Deleting ID:", id); // debug

    if (!confirm(`Are you sure you want to delete product ID: ${id}?`)) {
      return;
    }

    try {
      const res = await fetch(`/productos/${id}`, {
        method: 'DELETE'
      });

      if (res.status === 200) {
        const data = await res.json();
        alert('✅ ' + (data.message || 'Product deleted successfully!'));
      } else if (res.status === 404) {
        const data = await res.json();
        alert('❌ ' + (data.error || 'Product not found.'));
      } else {
        const data = await res.json();
        alert('❌ Server error: ' + (data.error || res.statusText));
      }

      // Close modal and refresh
      const modalEl = document.getElementById('deleteProductModal');
      const modal = bootstrap.Modal.getInstance(modalEl);
      modal.hide();

      form.reset();
      location.reload();
    } catch (err) {
      console.error("Error deleting:", err);
      alert('Connection error.');
    }
  });
});