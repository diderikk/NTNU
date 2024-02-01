<template>
    <div id="log">
        <h2>Log:</h2>
        <ul>
            <li v-for="(calculation, index) in calculations" :key="index">
                {{ calculation }}
            </li>
        </ul>
    </div>
</template>

<script>
export default{
    name: 'Log',
    props: ['calculations'],
    computed: {
        allCalculations(){
            return this.calculations.join('\n');
        }
    }
}
</script>

<style scoped>
    #log{
        font-size: 0.8em;
        width: 40%;
        border: 3px solid grey;
        outline: none;
        border-radius: 5px;
        padding: 10px 20px 10px 20px;
        resize: none;
        height: 30vh;
        overflow: scroll;
        overflow-x: hidden;
      }
</style>
