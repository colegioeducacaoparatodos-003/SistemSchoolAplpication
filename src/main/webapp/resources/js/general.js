$(document).ready(function () {
    stickNaveBarOnScroll()

})
//THIS HANDLES THE STICKY NAVBAR ON SCROLL
function stickNaveBarOnScroll() {

    // Home Page Navbar
    var $homeNavBar = $('.nav-bar');
    var $languageSwitcher = $('.language-switcher.ui-selectonemenu .ui-selectonemenu-label, .language-switcher.ui-selectonemenu .ui-selectonemenu-trigger .ui-icon');
    var $heroContent = $('.hero-content-container');
    var $headerElements = $('.nav-content, .main-nav');

    $(window).on('scroll', function () {
        if ($(this).scrollTop() > 80) {
            $homeNavBar.addClass('fixed-nav-bar');//this also change txt color in css
            $heroContent.addClass('support-fixed-nav-bar');
            $languageSwitcher.addClass('change-color');
            $headerElements.addClass('change-header-elements-border-color');
        } else {
            $homeNavBar.removeClass('fixed-nav-bar');
            $heroContent.removeClass('support-fixed-nav-bar');
            $languageSwitcher.removeClass('change-color');
            $headerElements.removeClass('change-header-elements-border-color');
        }
    });


};