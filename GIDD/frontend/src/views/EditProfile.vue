<template>
  <div id="edit-profile">
    <div class="nav">
      <button @click="profile" class="back-button">
        <i class="fa fa-arrow-left" aria-hidden="true"></i>
        Gå tilbake
      </button>
    </div>
    <h2>Instillinger</h2>
    <h3>Endre profilbilde</h3>
    <ImageSelector
      labelName=""
      @imageSelected="onSelectedImage"
      @removeImage="onRemoveImage"
    />
    <h3>Endre personlig informasjon</h3>
    <div id="form-container">
      <p class="form-title">Fornavn:</p>
      <input
        class="form-input"
        v-model="user.forename"
        type="name"
        placeholder="Nytt fornavn"
      />
      <p class="form-title">Etternavn:</p>
      <input
        class="form-input"
        v-model="user.surname"
        type="name"
        placeholder="Nytt etternavn"
      />
      <p class="form-title">E-post:</p>
      <input
        class="form-input"
        v-model="user.email"
        type="email"
        placeholder="Ny email"
      />
      <p v-if="emailIsNotEmpty && !isValidEmail">
        E-postadressen må inneholde @ og .
      </p>
    </div>
    <h3>Endre treningsnivå</h3>
    <div id="form-fitness-level">
      <label v-for="(trainingLevel, index) in trainingLevels" :key="index">
        <input
          id="fitness-input"
          type="radio"
          :value="selectedTrainingLevel"
          :checked="selectedTrainingLevel === trainingLevel.value"
          @change="changeTrainingLevel(trainingLevel.value)"
        />{{ trainingLevel.title }}
      </label>
    </div>
    <h3>Endre passord</h3>
    <div id="new-password-container">
      <div>
        <input
          class="password-form"
          v-model="password"
          type="password"
          placeholder="Nytt passord"
        />
        <p id="error-message" v-if="!passwordIsValid && passwordIsNotEmpty">
          ! Nytt passord må inneholde minst 8 tegn
        </p>
      </div>
      <div>
        <input
          class="password-form"
          v-model="repeatPassword"
          type="password"
          placeholder="Gjenta passord"
        />
        <p id="error-message" v-if="!matchingPasswords && passwordIsNotEmpty">
          ! Passordene må være like
        </p>
      </div>
    </div>

    <div id="old-password">
      <h4>Gammelt passord</h4>
      <input id="old-password-input" v-model="oldPassword" type="password" />
      <p v-if="!oldPasswordWasCorrect">
        Gammelt passord var ikke riktig. Besøk:
        <router-link to="/forgotten-password">Glemt passord</router-link> om du
        har glemt ditt gamle passord
      </p>
    </div>

    <div>
      <p v-if="passwordNotChanged && isProfileChanged">
        Personlig informasjon er endret, men ikke passord!
      </p>
      <p v-else-if="isProfileChanged">Profilen er endret!</p>
      <button
        id="change-profile-button"
        :disabled="!isValidForm"
        @click="saveProfileChanges"
        alt="Knapp som lagrer profilendringer"
      >
        Lagre endringer
      </button>
    </div>
    <div id="delete-user" @click="deleteUser">Slett brukeren</div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, Ref, ref } from "vue";
import { useRouter } from "vue-router";
import axios from "@/axiosConfig";
import { useStore } from "@/store";
import User from "@/interfaces/User/User.interface";
import EditUser from "@/interfaces/User/EditUser.interface";
import ImageSelector from "@/components/ImageSelector.vue";
import { TrainingLevel } from "@/enums/TrainingLevel.enum";

export default defineComponent({
  name: "EditProfile",
  components: { ImageSelector },
  setup() {
    const store = useStore();
    const router = useRouter();
    //Using ref since we are setting values
    const user: Ref<User> = ref(store.getters.user);

    /**
     * Checks if name is valid; that both name textareas are not empty
     */
    const isValidName = computed(() => {
      return (
        user.value.forename.trim() !== "" && user.value.surname.trim() !== ""
      );
    });

    /**
     * When image is selected
     */
    const onSelectedImage = (image: string) => {
      userDTO.profilePicture = image;
    };

    /**
     * When image is removed
     */
    const onRemoveImage = () => {
      delete userDTO.profilePicture;
    };

    //Password
    const password = ref("");
    const passwordFeedback = ref("");
    const repeatPassword = ref("");

    /**
     * Checks if the password and the repeat password areas
     * match
     */
    const matchingPasswords = computed(() => {
      return password.value == repeatPassword.value;
    });
    const numberOfCharacterPass = 8;

    /**
     * Checks if password is valid; that the number of characters
     * is 8 or above
     */
    const passwordIsValid = computed(() => {
      return password.value.length >= 8;
    });

    /**
     * Checks that the password is not empty
     */
    const passwordIsNotEmpty = computed(() => {
      return password.value.trim() !== "";
    });

    /**
     * Checks if password is valid; that password is not empty and that
     * the passwords match
     */
    const isValidPassword = computed(() => {
      return matchingPasswords.value && passwordIsNotEmpty.value;
    });

    /**
     * Makes the password feedback, either that it is missing
     * characters or if it is valid
     */
    const makePasswordFeedback = computed(() => {
      if (password.value.length < numberOfCharacterPass) {
        return (
          passwordFeedback.value +
          "Passordet må være minst " +
          numberOfCharacterPass +
          " tegn"
        );
      }
      return passwordFeedback.value + "Passordet er gyldig";
    });

    //Gammelt passord
    const oldPassword = ref("");

    /**
     * Checks that the old password area is not empty
     */
    const oldPasswordIsNotEmpty = computed(() => {
      return oldPassword.value.trim() !== "";
    });

    const oldPasswordWasCorrect = ref(true);

    //Email

    /**
     * Checks that email area is not empty
     */
    const emailIsNotEmpty = computed(() => {
      return user.value.email.trim() !== "";
    });

    /**
     * checks if email is valid; that it includes "@" and "."
     */
    const isValidEmail = computed(() => {
      return user.value.email.includes("@") && user.value.email.includes(".");
    });

    /**
     * Checks if the form is valid; that the password is empty or, that the password is valid
     * and old password is not empty and that the passwords match. Then it checks if the name
     * and email are valid
     */
    const isValidForm = computed(() => {
      return (
        (!passwordIsNotEmpty.value ||
          (isValidPassword.value &&
            oldPasswordIsNotEmpty.value &&
            passwordIsValid.value &&
            matchingPasswords.value)) &&
        isValidEmail.value &&
        isValidName.value
      );
    });

    /**
     * Delete user, first make a confirm window
     */
    const deleteUser = async (): Promise<void> => {
      if (window.confirm("Er du sikker på at du vil slette brukeren din")) {
        try {
          const response = await axios.delete(
            `/users/${store.getters.user.userId}`
          );
          if (response.status === 200) {
            store.dispatch("logout");
            router.replace("/log-in");
          } else if (response.status === 400) {
            oldPasswordWasCorrect.value = false;
          }
        } catch (error) {
          router.push("/error");
        }
      }
    };

    //Training level
    const trainingLevels = ref([
      { title: "Lav", value: TrainingLevel.LOW },
      { title: "Medium", value: TrainingLevel.MEDIUM },
      { title: "Høy", value: TrainingLevel.HIGH },
    ]);

    const changeTrainingLevel = (value: number) => {
      selectedTrainingLevel.value = value;
    };

    const isTrainingLevelValid = computed(() => {
      return selectedTrainingLevel.value != 0;
    });

    const trainingLevelAsString = computed(() => {
      switch (selectedTrainingLevel.value) {
        case 1:
          return "EASY";
        case 2:
          return "MEDIUM";
        case 4:
          return "HARD";
        default:
          return "";
      }
    });

    const trainingLevelAsNumber = computed(() => {
      switch (user.value.trainingLevel) {
        case "EASY":
          return 1;
        case "MEDIUM":
          return 2;
        case "HARD":
          return 4;
        default:
          return -1;
      }
    });

    const selectedTrainingLevel = ref(trainingLevelAsNumber.value);

    /**
     * EditUser object to send to backend
     */
    const userDTO: EditUser = {
      userId: user.value.userId,
      email: user.value.email,
      forename: user.value.forename,
      surname: user.value.surname,
      trainingLevel: user.value.trainingLevel,
    };

    const isProfileChanged = ref(false);
    const passwordNotChanged = ref(false);

    /**
     * Method to save the profile changes made. First checks if form is valid,
     * if so, starts saving. Password is not empty, add new and old password
     * to the editUser object. If not profile picture, set it to string "null".
     * Send response via axios, await response from update user
     * and set user to the new user
     */
    const saveProfileChanges = async (): Promise<void> => {
      oldPasswordWasCorrect.value = true;
      isProfileChanged.value = false;
      passwordNotChanged.value = false;
      if (isValidForm.value) {
        try {
          userDTO.userId = user.value.userId;
          userDTO.email = user.value.email;
          userDTO.forename = user.value.forename;
          userDTO.surname = user.value.surname;
          userDTO.trainingLevel = trainingLevelAsString.value;
          if (userDTO.newPassword || userDTO.oldPassword) {
            delete userDTO.newPassword;
            delete userDTO.oldPassword;
          }

          if (passwordIsNotEmpty.value) {
            userDTO.newPassword = password.value;
            userDTO.oldPassword = oldPassword.value;
          }
          if (!userDTO.profilePicture) userDTO.profilePicture = "null";

          const response = await axios.post(
            `/users/${userDTO.userId}`,
            userDTO
          );
          await store.dispatch("updateUser", response.data);
          user.value = store.getters.user;
          if (
            password.value.trim() === "" &&
            repeatPassword.value.trim() !== "" &&
            oldPassword.value.trim() !== ""
          ) {
            passwordNotChanged.value = true;
          }
          isProfileChanged.value = true;
        } catch (error) {
          if (error.response.status === 400) {
            oldPasswordWasCorrect.value = false;
          } else {
            router.push("/error");
          }
        }
      }
    };

    const profile = (): void => {
      router.back();
    };

    return {
      user,
      password,
      passwordIsValid,
      passwordFeedback,
      passwordIsNotEmpty,
      onSelectedImage,
      onRemoveImage,
      saveProfileChanges,
      makePasswordFeedback,
      emailIsNotEmpty,
      repeatPassword,
      matchingPasswords,
      deleteUser,
      isValidEmail,
      isValidForm,
      oldPassword,
      oldPasswordWasCorrect,
      trainingLevels,
      changeTrainingLevel,
      isTrainingLevelValid,
      selectedTrainingLevel,
      trainingLevelAsNumber,
      isProfileChanged,
      passwordNotChanged,
      profile,
    };
  },
});
</script>

<style scoped lang="scss">
@import url("https://fonts.googleapis.com/css2?family=Mulish&display=swap");

$primary-color: #282828;
$secondary-color: #ea4b4b;
$padding: 0.6rem 1rem 0.6rem 1rem;

#edit-profile {
  margin: 35px;
  @media only screen and (min-width: 600px) {
    width: 45%;
    margin: auto;
    padding: 20px;
  }
}

h3 {
  text-align: left;
  margin: 40px 0px 20px 0px;
}

#form-container {
  display: grid;
  grid-template-columns: 1fr 3fr;
  align-items: center;
  row-gap: 10px;
}

.form-title {
  text-align: left;
  grid-column: 1/2;
}

.form-input {
  grid-column: 2/3;
  width: 100%;
}

label {
  margin: 20px;
}

#form-fitness-level.input {
  margin: 10px;
}

#fitness-input {
  margin: 10px;
}

#new-password-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  column-gap: 10px;
  height: 80px;
}

.password-form {
  width: 100%;
  align-self: end;
}

#error-message {
  font-weight: 600;
  color: $secondary-color;
  font-size: 10px;
  line-height: 20px;
}

#old-password {
  text-align: left;
}

#old-password-input {
  width: 45%;
  margin-bottom: 20px;
}

#change-profile-button {
  margin: 20px;
}

#delete-user {
  font-size: 10px;
  text-transform: uppercase;
  font-weight: 600;
}

#delete-user:hover {
  text-decoration: underline;
  color: $secondary-color;
  cursor: pointer;
}

.back-button {
  color: $primary-color;
  background-color: unset;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: 600;
  font-size: 0.7rem;
  width: 130px;
  text-align: left;
  padding: 0;
  display: block;
}

.back-button:hover {
  color: $secondary-color;
}
</style>
