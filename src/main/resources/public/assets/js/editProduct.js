document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('editProductForm');
  const idInput = document.getElementById('editId');

  async function loadProductData(id) {
    if (!id) return;

    try {
      const response = await fetch(`/productos/${id}`);
      if (!response.ok) {
        alert('❌ Product not found.');
        document.getElementById('editId').value = id; // leave the typed ID
        return;
      }

      const product = await response.json();
      document.getElementById('editNombre').value = product.nombre || '';
      document.getElementById('editDescripcion').value = product.descripcion || '';
      document.getElementById('editCosto').value = product.costo || '';
      document.getElementById('editCantidad').value = product.cantidad || '';

    } catch (err) {
      console.error(err);
      alert('Error loading product data.');
    }
  }

  // 🔹 Load product info when the user inputs or leaves the ID field
  idInput.addEventListener('change', (e) => {
    const id = parseInt(e.target.value);
    if (id > 0) loadProductData(id);
  });

  idInput.addEventListener('blur', (e) => {
    const id = parseInt(e.target.value);
    if (id > 0) loadProductData(id);
  });

  // 🔹 Submit updated data
  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = parseInt(document.getElementById('editId').value);
    const updatedProduct = {
      nombre: document.getElementById('editNombre').value,
      descripcion: document.getElementById('editDescripcion').value,
      costo: parseFloat(document.getElementById('editCosto').value),
      cantidad: parseInt(document.getElementById('editCantidad').value)
    };

    try {
      const response = await fetch(`/productos/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedProduct)
      });

      if (!response.ok) {
        const errorData = await response.json();
        alert('Error updating product: ' + (errorData.error || response.statusText));
        return;
      }

      alert('✅ Product updated successfully!');
      const modalEl = document.getElementById('editProductModal');
      const modal = bootstrap.Modal.getInstance(modalEl);
      modal.hide();

      form.reset();
      location.reload();
    } catch (err) {
      console.error(err);
      alert('Connection error while updating the product.');
    }
  });
});