const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            data: [],
            loans: [],
            accounts : [],
            totalBalance : 0,
            params: "",
            id: "",
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            this.params = new URLSearchParams(location.search);
            this.id = this.params.get("id");
            axios.get('http://localhost:8080/api/clients/' + this.id)
                .then(response => {
                    this.data = response.data
                    this.loans = this.data.loans
                    this.accounts = this.data.accounts
                    console.log(this.data);

                    for (account of this.data.accounts){
                        this.totalBalance += account.balance;
                    }

                    console.log(this.totalBalance);
                    
                })
                .catch(error => console.log(error));
        },
    }
}).mount("#app");