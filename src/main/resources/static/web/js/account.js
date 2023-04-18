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
        }
    },
    created(){
        this.loadData()
    },
    methods: {
        loadData(){
            this.params = new URLSearchParams(location.search);
            this.id = this.params.get("id");
            axios.get('http://localhost:8080/api/accounts/' + this.id)
            .then(response => {
                this.data = response.data;
                this.filterData = response.data.transactions;

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
        deleteClient(){

        }
    },
}).mount("#app")