$(document).ready(function () {
    AOS.init({
        duration: 300,
    });
    stickNaveBarOnScroll();
    handleContactForm();

    const year = new Date().getFullYear();
    $("#currentYear").text(year);

})

//THIS HANDLES THE STICKY NAVBAR ON SCROLL
function stickNaveBarOnScroll() {

    // Home Page Navbar
    var $homeNavBar = $('.nav-bar');
    var $languageSwitcher = $('.language-switcher.ui-selectonemenu .ui-selectonemenu-label, .language-switcher.ui-selectonemenu .ui-selectonemenu-trigger .ui-icon');
    var $heroContent = $('.hero-content-container');
    var $headerElements = $('.nav-content, .main-nav');
    var $publicPagesContent = $('.news-page-container, .show-news-page');

    $(window).on('scroll', function () {
        if ($(this).scrollTop() > 80) {
            $homeNavBar.addClass('fixed-nav-bar');//this also change txt color in css
            $heroContent.addClass('support-fixed-nav-bar');
            $languageSwitcher.addClass('change-color');
            $headerElements.addClass('change-header-elements-border-color');
            $publicPagesContent.addClass('public-page-support-nav-bar');
        } else {
            $homeNavBar.removeClass('fixed-nav-bar');
            $heroContent.removeClass('support-fixed-nav-bar');
            $languageSwitcher.removeClass('change-color');
            $headerElements.removeClass('change-header-elements-border-color');
            $publicPagesContent.removeClass('public-page-support-nav-bar');
        }
    });


};

//this handles the left side dialog in home page
function handleContactForm() {
    const overlay = $(".contactFormOverLay");

    const openOverlay = () => {
        overlay.css("right", "0");
        $("body").css("overflow", "hidden"); // 🔒 bloqueia scroll
    };

    const closeOverlay = () => {
        overlay.css("right", "-100%");
        $("body").css("overflow", ""); // 🔓 restaura scroll
    };

    $("#openContactForm").on("click", openOverlay);
    $("#closeContactForm, .contactFormOverLay").on("click", closeOverlay);
    $(".contactForm").on("click", e => e.stopPropagation());
}

//this code helps the CSS on nav-links'hover
$(document).ready(function () {

    $(".nav-link").mouseenter(function () {
        $(this).find(".nav-links-group").css("display", "flex");
    });

    $(".nav-link").mouseleave(function () {
        $(this).find(".nav-links-group").css("display", "none");
    });

});


//this functions handles carousel component for heigh-lights in landing page
document.addEventListener("DOMContentLoaded", function () {
    let index = 0;
    let slideInterval; // Variable to store the timer
    const slidesContainer = document.querySelector(".heigh-light-slides");
    const slides = document.querySelectorAll(".heigh-light-slide");
    const carouselWrapper = document.querySelector(".heigh-light-carousel");

    if (slides.length > 0) {
        // 1. Clone first slide for the infinite loop
        const firstClone = slides[0].cloneNode(true);
        slidesContainer.appendChild(firstClone);

        const allSlides = document.querySelectorAll(".heigh-light-slide");
        const totalSlides = allSlides.length;

        function moveSlide() {
            index++;
            slidesContainer.style.transition = "transform 0.6s ease-in-out";
            slidesContainer.style.transform = `translateX(-${index * 100}%)`;

            if (index === totalSlides - 1) {
                setTimeout(() => {
                    slidesContainer.style.transition = "none";
                    slidesContainer.style.transform = "translateX(0)";
                    index = 0;
                }, 600);
            }
        }

        // 2. Function to start the auto-play
        function startInterval() {
            slideInterval = setInterval(moveSlide, 5000);
        }

        // 3. Function to stop the auto-play
        function stopInterval() {
            clearInterval(slideInterval);
        }

        // 4. Event Listeners for Hover
        carouselWrapper.addEventListener("mouseenter", stopInterval);
        carouselWrapper.addEventListener("mouseleave", startInterval);

        // Initialize the first start
        startInterval();
    }
});


