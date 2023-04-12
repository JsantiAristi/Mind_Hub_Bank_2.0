const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            passwordID: "",
        }
    },
}).mount("#app")

var swiper = new Swiper(".mySwiper", {
    pagination: {
    el: ".swiper-pagination",
    dynamicBullets: true,
    },
});