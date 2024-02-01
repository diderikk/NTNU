<template>
  <div id="dots-container">
    <button
      v-for="index in numberOfButtons"
      :key="index"
      :disabled="maxStageAllowed < index"
      :class="{
        available: currentStage !== index,
      }"
      class="completion-dot"
      @click="changeStage(index)"
    ></button>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
export default defineComponent({
  name: "PageDots",
  props: {
    numberOfButtons: {
      required: true,
      type: Number,
    },
    maxStageAllowed: {
      required: true,
      type: Number,
    },
    currentStage: {
      required: true,
      type: Number,
    },
  },
  setup(props, { emit }) {
    const changeStage = (index: number) => {
      emit("changeStage", index);
    };

    return {
      changeStage,
    };
  },
});
</script>

<style scoped lang="scss">
$secondary-color: #ea4b4b;
$disabled-color: #d7dce6;
button.completion-dot {
  height: 15px;
  width: 15px;
  border-radius: 20px;
  margin: 5px;
  display: block;
  background: $secondary-color;
  padding: 0px;
}

button.available {
  border: 2px $secondary-color solid;
  background-color: #ffffff;
  opacity: 100%;
}

#dots-container {
  display: grid;
  padding-top: 10px;
  margin: auto;
  grid-template-columns: 1fr 1fr 1fr 1fr;
  width: 100px;
  justify-content: center;
}

button.completion-dot:disabled {
  background-color: $disabled-color;
  color: #9499a5;
}
</style>
