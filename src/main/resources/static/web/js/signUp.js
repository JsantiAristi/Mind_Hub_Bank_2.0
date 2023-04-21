const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            firstName: "",
            secondName: "",
            emailAdress: "",
            password: ""
        }
    },
    methods: {
        signUp(){
            axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.secondName}&emailAdress=${this.emailAdress}&password=${this.password}`)
            .then(response => 
                axios.post('/api/login',`emailAdress=${this.emailAdress}&password=${this.password}`)
                .then(response => window.location.href="/web/pages/accounts.html")
                .catch(error => console.log(error)) )
            .catch(error => console.log(error))
        }
    }
}).mount("#app");