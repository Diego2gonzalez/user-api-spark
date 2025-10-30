document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('addProductForm');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const product = {
      nombre: document.getElementById('nombre').value,
      descripcion: document.getElementById('descripcion').value,
      costo: parseFloat(document.getElementById('costo').value),
      cantidad: parseInt(document.getElementById('cantidad').value)
    };

    try {
      const response = await fetch('/productos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(product)
      });

      if (!response.ok) {
        const errorData = await response.json();
        alert('Error: ' + (errorData.error || response.statusText));
        return;
      }

      const newProduct = await response.json();
      console.log('Added:', newProduct);
      alert('✅ Product added successfully!');

      // Close modal
      const modalEl = document.getElementById('addProductModal');
      const modal = bootstrap.Modal.getInstance(modalEl);
      modal.hide();

      form.reset();
      // refresh product list if you're viewing /products
      location.reload();

    } catch (err) {
      console.error(err);
      alert('Connection error while adding the product.');
    }
  });
});