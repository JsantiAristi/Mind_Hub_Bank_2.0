const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            data: [],
            largeScreenSize: 768,
            screenWidth: 0,
            checked: [],
            firstName : "",
            lastName : "",
            email: "",
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get('http://localhost:8080/api/clients/current')
                .then(response => {
                    console.log(response.data);
                    this.data = response.data
                })
                .catch(error => console.log(error));
        },
        singOut() {
            Swal.fire({
                title: 'Are you sure that you want to log out?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                preConfirm: () => {
                    return axios.post('/api/logout')
                        .then(response => {
                            window.location.href="/index.html"
                        })
                        .catch(error => {
                            Swal.showValidationMessage(
                                `Request failed: ${error}`
                            )
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        },
        handleResize() {
            this.screenWidth = window.innerWidth;
        }
    },
    computed: {
        showElement() {
            return this.screenWidth >= this.largeScreenSize;
        }
    },
    mounted() {
        this.screenWidth = window.innerWidth;
        window.addEventListener('resize', this.handleResize);
    },
    destroyed() {
        window.removeEventListener('resize', this.handleResize);
    },
}).mount("#app");

window.onload = function(){
    $('#onload').fadeOut();
    $('body').removeClass("hidden");
}