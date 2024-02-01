<template>
  <div id="message">
    <div id="message-info">
      <p>
        <b>{{ messageData.forename }}</b> {{ messageData.time }}:
      </p>
    </div>
    <div id="message-content">
      {{ messageData.message }}
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import IMessage from "@/interfaces/IMessage.interface";

export default defineComponent({
  name: "Message",
  props: {
    messageData: {
      type: Object as () => IMessage,
      required: true,
    },
  },
});
</script>

<style scoped lang="scss">
$primary-color: #282828;
$secondary-color: #ea4b4b;
$padding: 0.6rem 1rem 0.6rem 1rem;

#message {
  width: 60%;
  float: right;
}

p {
  font-size: 0.85rem;
}

#message-content {
  background-color: $secondary-color;
  border-radius: 20px;
  padding: $padding;
  color: #ffffff;
  margin-bottom: 10px;
}
</style>