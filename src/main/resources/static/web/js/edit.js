const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            data: [],
            largeScreenSize: 768,
            screenWidth: 0,
            checked: "",
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
        changeInfo(){
            if(this.firstName.length == 0){ this.firstName = this.data.firstName }
            if(this.lastName.length == 0){ this.lastName = this.data.lastName }
            if(this.email.length == 0){ this.email = this.data.emailAddress }
            if(this.checked.length == 0){ this.checked = this.data.image }
            Swal.fire({
                title: 'Are you sure that you want to change the information',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                preConfirm: () => {
                    return axios.put('/api/clients' , 
                    {
                        "firstName": this.firstName,
                        "lastName": this.lastName,
                        "emailAddress": this.email,
                        "image": this.checked
                    })
                        .then(response => {
                            window.location.href="/web/pages/accounts.html"
                        })
                        .catch(error => {
                            Swal.showValidationMessage(
                                `Request failed: ${error}`
                            )
                            console.log(error);
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