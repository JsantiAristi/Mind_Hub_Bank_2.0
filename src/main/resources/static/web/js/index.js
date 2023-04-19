const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            passwordID: "",
        }
    },
}).mount("#app");

const mq = window.matchMedia("(max-width: 700px)")

function pantallaPequena(mq){
    if (mq.matches) {
        const swiper = new Swiper(".mySwiper", {
            slidesPerView: 1,
            spaceBetween: 30,
            pagination: {
                el: ".swiper-pagination",
                clickable: true,
            },
            navigation: {
                nextEl: '.swiper-button-next',
                prevEl: '.swiper-button-prev',
            },
        });
    } else {
        const swiper = new Swiper(".mySwiper", {
            slidesPerView: 2,
            spaceBetween: 30,
            pagination: {
                el: ".swiper-pagination",
                clickable: true,
            },
            navigation: {
                nextEl: '.swiper-button-next',
                prevEl: '.swiper-button-prev',
            },
        });
    }
}

mq.addListener(pantallaPequena);

pantallaPequena(mq);
