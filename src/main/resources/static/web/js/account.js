const { createApp } = Vue

createApp({
    data(){
        return {
    // Inicializamos las variables
        data: [],
        params: "",
        id: "",
        checked : "",
        filterData : [],
        selectInput: "Open this select menu",
        totalBalance: 0,
        }
    },
    created(){
        this.loadData()
    },
    methods: {
        loadData(){
            this.params = new URLSearchParams(location.search);
            this.id = this.params.get("id");
            axios.get('http://localhost:8080/api/clients/current/accounts/' + this.id)
            .then(response => {
                this.data = response.data;
                this.totalBalance = this.data.balance;
                this.filterData = response.data.transactions;
                console.log(this.data);

                this.datos.transactions.sort((transaction1, transaction2) => {
                    return (transaction2.date.slice(0,4) + transaction2.date.slice(5,7) + transaction2.date.slice(8,10)) - (transaction1.date.slice(0,4) + transaction1.date.slice(5,7) + transaction1.date.slice(8,10));
                })
            })
            .catch(error => console.log(error));
        },
        filtroCard(){
            if (this.checked == "ALL") {
                this.filterData = this.data.transactions;
            } else {
                this.filterData = this.data.transactions.filter(account => {
                    return this.checked.includes(account.type)
                });
            }  
        },
        filtroDate(){
            if (this.selectInput == 1) {
                this.filterData.sort((transaction1, transaction2) => {;
                    return (transaction2.date.slice(0,4) + transaction2.date.slice(5,7) + transaction2.date.slice(8,10)) - (transaction1.date.slice(0,4) + transaction1.date.slice(5,7) + transaction1.date.slice(8,10));
                })
            } else if(this.selectInput == 2){
                this.filterData.sort((transaction1, transaction2) => {;
                    return (transaction1.date.slice(0,4) + transaction1.date.slice(5,7) + transaction1.date.slice(8,10)) - (transaction2.date.slice(0,4) + transaction2.date.slice(5,7) + transaction2.date.slice(8,10));
                })
            }
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
    },
}).mount("#app")

window.onload = function(){
    $('#onload').fadeOut();
    $('body').removeClass("hidden");
}