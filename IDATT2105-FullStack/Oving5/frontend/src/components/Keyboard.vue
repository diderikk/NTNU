<template>
  <ul id="keys">
    <li class="button-list" v-for="item in items" :key="item">
      <button class="button" :class="{equals: item === '='}" @click="clickHandle">{{ item }}</button>
    </li>
  </ul>
</template>

<script>
export default {
  name: "Keyboard",
  data() {
    return {
      items: [
        "C", "ANS", "DEL", "+", "1", "2", "3","-",
        "4", "5", "6", "*", "7", "8", "9", "/", "#",
        "0", ".", "=",
      ],
    };
  },
  methods: {
    clickHandle(event) {
      this.$emit("keyClick", event.target.innerHTML);
    },
  },
};
</script>

<style scoped>
    #keys {
    display: grid;
    width: 25%;
    grid-template-columns: repeat(4, 1fr);
    gap: 5px;
    margin-bottom: 3%;
    padding: 0;
    }
    .button-list{
        list-style-type: none;

    }
    .button{
        height: 100%;
        width: 100%;
    }
    .equals{
        background: grey;
    }
</style>
