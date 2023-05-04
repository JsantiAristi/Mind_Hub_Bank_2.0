const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            data: [],
            selectInput: "",
            checked : "",
            amount: "",
            dataFilter : [],
        }
    },
    created() {
        this.dataLoan();
    },
    methods: {
        dataLoan(){
            axios.get("/api/loans")
            .then(response => {
                this.data = response.data;
                console.log(this.data);
            })
            .catch(error => console.log(error))
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
        filterSelector(){
            this.dataFilter = this.data.filter( loan => {
                return this.checked.includes(loan.name)
            })
            console.log(this.dataFilter);
        },
    },
}).mount("#app");

window.onload = function(){
    $('#onload').fadeOut();
    $('body').removeClass("hidden");
}