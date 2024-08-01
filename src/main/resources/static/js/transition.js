document.addEventListener("DOMContentLoaded", function() {
  if (sessionStorage.getItem("animationDone") !== "true") {
    const animatables = document.querySelectorAll(".animatable");
    animatables.forEach(el => {
      el.classList.add("active");
    });

    sessionStorage.setItem("animationDone", "true");

    animatables.forEach(el => {
      el.addEventListener('animationend', () => {
        el.classList.remove('active');
      });
    });
  }
});