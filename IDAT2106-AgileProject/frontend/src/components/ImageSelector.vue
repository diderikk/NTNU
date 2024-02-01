<template>
  <div class="container">
    <img v-if="imageCheck" :src="imageData" alt="Profilbildet visining:" />
    <img v-else src="../../img/placeholder.png" />
    <form @submit.prevent="">
      <div class="form-group" id="file-button-group">
        <div id="label-input">
          <label for="fileInput">{{ labelName }}</label>
          <input
            ref="fileInput"
            id="fileInput"
            type="file"
            @change="uploadFile"
          />
        </div>
      </div>
      <button @click="removeImage">
        <i class="fa fa-times" aria-hidden="true"></i> Fjern bildet
      </button>
    </form>
  </div>
</template>

<script lang="ts">
import { defineComponent, Ref, ref } from "vue";

export default defineComponent({
  name: "ImageSelector",
  props: {
    labelName: String,
  },
  setup(props, { emit }) {
    const imageData = ref("") as Ref<string | ArrayBuffer | null | undefined>;
    const fileInput = ref(null) as Ref<any>;
    const imageCheck = ref(false);

    const uploadFile = (event: any) => {
      let input = event.target.files[0];
      if (input) {
        let reader = new FileReader();
        reader.onload = (e) => {
          imageData.value = e.target?.result;
          emit("imageSelected", imageData.value);
          imageCheck.value = true;
        };
        reader.readAsDataURL(input);
      }
    };

    const removeImage = () => {
      imageData.value = "";
      fileInput.value.value = "";
      emit("removeImage");
      imageCheck.value = false;
    };

    return {
      uploadFile,
      imageData,
      removeImage,
      fileInput,
      imageCheck,
    };
  },
});
</script>

<style scoped>
.container {
  text-align: center;
  justify-items: center;
  width: 100%;
}

#fileInput, label {
  display: block;
  text-align: left;
  font-size: 1rem;
  margin-bottom: 10px;
  width: 100%;
}

label {
  font-weight: 600;
}

img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  margin: 20px;
}

input {
  border-radius: 100px;
}

#label-input {
  display: inline-block;
}

button {
  background-color: unset;
  color: #282828;
  margin: 5px;
}

button:hover {
  cursor: pointer;
  color: #ea4b4b;
}
</style>
