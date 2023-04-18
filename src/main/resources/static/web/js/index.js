const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            passwordID: "",
        }
    },
}).mount("#app");

const swiper = new Swiper(".mySwiper", {
    pagination: {
        el: ".swiper-pagination",
        dynamicBullets: true,
        type: 'bullets',
    },
    navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev',
    },
});
