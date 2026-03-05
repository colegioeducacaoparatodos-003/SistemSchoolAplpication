$(document).ready(function () {
    stickNaveBarOnScroll()

    $('.owl-carousel').owlCarousel({
        loop: true,
        margin: 10,
        nav: true,
        responsive: {
            0: {
                items: 1
            },
            600: {
                items: 3
            },
            1000: {
                items: 5
            }
        }
    })

})
//THIS HANDLES THE STICKY NAVBAR ON SCROLL
function stickNaveBarOnScroll() {

    // Home Page Navbar
    var $homeNavBar = $('.nav-bar');
    var $heroContent = $('.hero-content-container');
    var $headerElements = $('.nav-content, .main-nav');

    $(window).on('scroll', function () {
        if ($(this).scrollTop() > 80) {
            console.log('hero');
            $homeNavBar.addClass('fixed-nav-bar');
            $heroContent.addClass('support-fixed-nav-bar');
            $headerElements.addClass('change-header-elements-border-color');
        } else {
            $homeNavBar.removeClass('fixed-nav-bar');
            $heroContent.removeClass('support-fixed-nav-bar');
            $headerElements.removeClass('change-header-elements-border-color');
        }
    });


};