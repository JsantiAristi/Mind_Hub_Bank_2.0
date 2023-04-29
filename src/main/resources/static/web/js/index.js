const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            emailAdress: "",
            password: "",
        }
    },
    methods: {
        signIn(){
            axios.post('/api/login',`emailAdress=${this.emailAdress}&password=${this.password}`)
            .then(response => {
                Swal.fire({
                    icon: 'success',
                    text: 'You enter in your account succesfully',
                    showConfirmButton: false,
                    timer: 2000,
                }).then( () => window.location.href="/web/pages/accounts.html")
            })
            .catch(error => {
                Swal.fire({
                    icon: 'error',
                    text: 'Wrong password',
                    confirmButtonColor: "#7c601893",
                })
                console.log(error);
            })         
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

window.onload = function(){
    $('#onload').fadeOut();
    $('body').removeClass("hidden");
}