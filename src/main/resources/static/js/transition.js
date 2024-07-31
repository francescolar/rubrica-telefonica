document.addEventListener("DOMContentLoaded", function() {
  // Verifica se l'animazione è già stata eseguita nella sessione corrente
  if (sessionStorage.getItem("animationDone") !== "true") {
    // Se non è stata eseguita, aggiungi la classe 'active' agli elementi
    const animatables = document.querySelectorAll(".animatable");
    animatables.forEach(el => {
      el.classList.add("active");
    });

    // Imposta un valore nel sessionStorage per indicare che l'animazione è stata eseguita
    sessionStorage.setItem("animationDone", "true");

    // Ascolta la fine dell'animazione e poi rimuovi la classe 'active'
    animatables.forEach(el => {
      el.addEventListener('animationend', () => {
        el.classList.remove('active');  // Rimuove 'active' al termine dell'animazione
      });
    });
  }
});