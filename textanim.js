document.addEventListener('DOMContentLoaded', function() {
  const container = document.getElementById('marqueeContainer');
  const text = document.getElementById('marqueeText');

  // Funkcja do aktualizacji wymiarów
  function updateDimensions() {
    containerWidth = container.offsetWidth;
    textWidth = text.offsetWidth;
  }
  let containerWidth, textWidth;
  updateDimensions();

  // Początkowe ustawienia
  let pos = 0;                      // aktualna pozycja (w px)
  const baseSpeed = 1.5;            // bazowa prędkość (px/klatkę przy 60fps)
  let speed = baseSpeed;            // aktualna prędkość (dodatnia: w prawo, ujemna: w lewo)
  const pauseDuration = 500;        // pauza w ms
  let paused = false;               // flaga pauzy
  let lastTimestamp = null;         // do obliczania delta czasu

  /**
   * animate - główna funkcja animacyjna
   * @param {DOMHighResTimeStamp} timestamp
   */
  function animate(timestamp) {
    if (!lastTimestamp) lastTimestamp = timestamp;
    const delta = timestamp - lastTimestamp;  // czas od ostatniej klatki
    lastTimestamp = timestamp;

    if (!paused) {
      // Przemieszczamy tekst proporcjonalnie do delta
      pos += speed * (delta / 16.67); // 16.67ms ~ 60fps

      // Zawijanie: gdy tekst całkowicie wychodzi poza ekran, resetujemy pozycję
      if (speed > 0 && pos > containerWidth) {
        pos = -textWidth;
      } else if (speed < 0 && pos < -textWidth) {
        pos = containerWidth;
      }
      text.style.transform = `translateX(${pos}px)`;
    }
    requestAnimationFrame(animate);
  }

  /**
   * handlePauseAndReverse - pauzuje animację i zmienia kierunek po danym czasie
   * @param {number} newSpeed - docelowa prędkość po pauzie
   */
  function handlePauseAndReverse(newSpeed) {
    if (!paused) {
      paused = true;
      // Pauza przez pauseDuration ms, potem zmiana kierunku bez resetu pozycji
      setTimeout(() => {
        speed = newSpeed;
        paused = false;
      }, pauseDuration);
    }
  }

  // Po najechaniu kursorem – zatrzymaj, potem zmień kierunek na lewo
  container.addEventListener('mouseenter', function() {
    // Nie restartujemy, tylko pauzujemy i ustawiamy speed na ujemną wartość
    handlePauseAndReverse(-Math.abs(baseSpeed));
  });

  // Po opuszczeniu kursora – zatrzymaj, potem zmień kierunek na prawo
  container.addEventListener('mouseleave', function() {
    handlePauseAndReverse(Math.abs(baseSpeed));
  });

  // Aktualizacja wymiarów przy resize
  window.addEventListener('resize', updateDimensions);

  requestAnimationFrame(animate);
});
