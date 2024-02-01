<template>
  <main>
    <h1>{{ title }}</h1>
    <h2 v-if="showAnswer" :class="{ error: isError }">{{ answer }}</h2>
    <h2 v-else-if="input.length">{{ currentCalc }}</h2>
    <h2 v-else>0</h2>
    <Keyboard @keyClick="handleKeyClick" />
    <Log :calculations="calculations" />
  </main>
</template>

<script>
import Keyboard from "./components/Keyboard";
import Log from "./components/Log";

export default {
  name: "App",
  components: { Keyboard, Log },
  data() {
    return {
      title: "Calculator",
      image: "./assets/calculator.jpg",
      input: [],
      answer: "0",
      showAnswer: false,
      calculations: [],
      isError: false,
    };
  },
  methods: {
    handleKeyClick(key) {
      this.showAnswer = false;
      this.isError = false;
      if (
        (key === "*" || key === "/" || key === "+" || key === "-") &&
        this.answer != "0" &&
        this.input.length === 0
      ) {
        this.input.push("ANS", key);
        return;
      }
      switch (key) {
        case "DEL":
          this.input.pop();
          break;
        case "C":
          this.input = [];
          break;
        case "=":
          this.handleEquals(key);
          break;
        default:
          this.input.push(key);
      }
    },
    handleEquals(key) {
      try {
        this.answer = eval(this.getFullCalculation);
        if (this.answer % 1 === 0) this.answer = this.answer.toString();
        else this.answer = this.answer.toFixed(3);

        this.input.push(" ", key, " ", this.answer, " ");

        this.calculations.unshift(this.currentCalc);
      } catch (error) {
        if (error instanceof SyntaxError) {
          console.log(error.name + ":", "Error in your syntax");
          this.answer = "Error in your syntax";
        } else {
          console.log(error.name + ":", error.message);
          this.answer = "Error in your syntax";
        }
        this.isError = true;
      }
      this.showAnswer = true;
      this.input = [];
    },
  },
  computed: {
    currentCalc() {
      return this.input.join("");
    },
    getFullCalculation() {
      let answer = this.answer;
      this.input.forEach(function (item, index) {
        if (item === "ANS") {
          this[index] = answer;
        }
      }, this.input);
      return this.input.join("");
    },
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  font-size: 1.6em;
}
main {
  display: flex;
  flex-direction: column;
  align-items: center;
}
h2,
p {
  margin: 0;
}
.error {
  color: red;
}
</style>
