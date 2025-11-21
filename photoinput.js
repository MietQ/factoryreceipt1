// Definiujemy funkcję, która dodaje obsługę wyboru pliku
function initializePhotoInput() {
  console.log("initializePhotoInput called");

  // Szukamy przycisku do uploadu pliku
  const fileUploadBtn = document.querySelector('.file-upload-btn');
  console.log("initializePhotoInput: fileUploadBtn:", fileUploadBtn);

  if (fileUploadBtn) {
    fileUploadBtn.addEventListener('click', function(e) {
      e.preventDefault();
      const fileInput = document.getElementById('fileUpload');
      console.log("initializePhotoInput: fileInput (on click):", fileInput);
      if (fileInput) {
        fileInput.click();
      } else {
        console.error("Nie znaleziono #fileUpload przy kliknięciu!");
      }
    });
  } else {
    console.error("Nie znaleziono elementu .file-upload-btn podczas init");
  }

  // Szukamy inputa typu file
  const fileInput = document.getElementById('fileUpload');
  console.log("initializePhotoInput: fileInput (global):", fileInput);
  if (!fileInput) {
    console.error("Nie znaleziono #fileUpload przy init");
    return;
  }

  fileInput.addEventListener('change', function(e) {
    const file = e.target.files[0];
    console.log("initializePhotoInput: Wybrano plik:", file);
    if (file) {
      const formData = new FormData();
      formData.append('file', file);
      console.log("initializePhotoInput: Wysyłanie pliku...");
      fetch('/api/uploadImage', {
        method: 'POST',
        body: formData
      })
      .then(response => {
        if (!response.ok) {
          throw new Error("Błąd uploadu: " + response.status);
        }
        return response.json();
      })
      .then(data => {
        console.log("initializePhotoInput: Odpowiedź z serwera:", data);
        const photoInput = document.getElementById('photo');
        if (photoInput && data.url) {
          photoInput.value = data.url;
          console.log("initializePhotoInput: Ustawiono URL:", data.url);
        } else {
          console.error("Nie znaleziono #photo lub brak data.url");
        }
      })
      .catch(err => {
        console.error("initializePhotoInput: Upload error:", err);
        alert("Błąd przy wysyłaniu pliku: " + err.message);
      });
    }
  });
}

// Jeśli elementy są już w DOM przy załadowaniu, można je od razu zainicjalizować
document.addEventListener('DOMContentLoaded', function() {
  console.log("photoinput.js: DOMContentLoaded fired");
  if (document.querySelector('.file-upload-btn') && document.getElementById('fileUpload')) {
    initializePhotoInput();
  } else {
    console.log("photoinput.js: Elementy .file-upload-btn lub #fileUpload nie są obecne przy inicjalizacji");
  }
});

// Umożliwiamy wywołanie funkcji globalnie, aby można było ją wywołać po dynamicznym dodaniu formularza
window.initializePhotoInput = initializePhotoInput;
