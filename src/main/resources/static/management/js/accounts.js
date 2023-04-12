const { createApp } = Vue

createApp({
    data(){
        return {
    // Inicializamos las variables
        data: [],
        params: "",
        id: "",
        }
    },
    created(){
        this.loadData()
    },
    methods: {
        loadData(){
            this.params = new URLSearchParams(location.search);
            this.id = this.params.get("id");
            axios.get('http://localhost:8080/api/clients/' + this.id)
            .then(response => {
                console.log(response.data);
                this.data = response.data
            })
            .catch(error => console.log(error));
        },
    }
}).mount("#app")