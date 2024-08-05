// sessionStorage.clear();


document.addEventListener('DOMContentLoaded', function() {
    const themeButton = document.getElementById('theme-toggle');
    const themeStylesheet = document.getElementById('theme-stylesheet-color');
    const homepageBackground = document.querySelector('html');
    const themeToggleButton = document.getElementById("theme-icon");
  
    function applySavedTheme() {
        const savedTheme = localStorage.getItem('theme');
        const savedIcon = localStorage.getItem('themeIcon');
        if (savedTheme) {
            themeStylesheet.setAttribute('href', savedTheme);
            updateHomepageBackground(savedTheme);
        }

        if (savedIcon) {
            themeToggleButton.innerHTML = savedIcon;
        }
    }
  
    function updateHomepageBackground(themePath) {
        if (homepageBackground) {
            if (themePath === '/css/style-dark.css') {
                homepageBackground.style.backgroundImage = "url('/img/site/texture1-nobg.png')";
            } else {
                homepageBackground.style.backgroundImage = "url('/img/site/texture1-nobg-25op.png')";
            }
        }
    }
  
    function toggleTheme(event) {
        event.preventDefault();
        if (themeToggleButton.innerHTML === 'dark_mode') {
          themeToggleButton.innerHTML = "light_mode";
        } else {
          themeToggleButton.innerHTML = "dark_mode";
        }
  
        let newTheme;
        if (themeStylesheet.getAttribute('href') === '/css/style-light.css') {
            newTheme = '/css/style-dark.css';
        } else {
            newTheme = '/css/style-light.css';
        }
  
        
        themeStylesheet.setAttribute('href', newTheme);
        localStorage.setItem('theme', newTheme);
        localStorage.setItem('themeIcon', themeToggleButton.innerHTML);
        updateHomepageBackground(newTheme);
    }
  
    applySavedTheme();
    themeButton.addEventListener('click', toggleTheme);
  });
  