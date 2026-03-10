$(document).ready(function () {
    AOS.init({
        duration: 300,
    });
    stickNaveBarOnScroll();
    handleContactForm();

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
$(document).ready(function(){

    $(".nav-link").mouseenter(function(){
        $(this).find(".nav-links-group").css("display","flex");
    });

    $(".nav-link").mouseleave(function(){
        $(this).find(".nav-links-group").css("display","none");
    });

});

