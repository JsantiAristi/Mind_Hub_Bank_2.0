const { createApp } = Vue;

createApp({
    data() {
        return {
            datos: [],
            firstname: "",
            lastname: "",
            emailAddress: "",
            clienteFiltrado: {},
        }
    },
    created() {
        this.loadData();
    },
    methods: {
        loadData() {
            axios.get('http://localhost:8080/api/clients')
                .then(response => {
                    console.log(response);
                    this.datos = response.data;
                })
                .catch(error => console.log(error));
        },
        addClient(){
        },
        deleteClient(){
        }
    },
}).mount("#app");