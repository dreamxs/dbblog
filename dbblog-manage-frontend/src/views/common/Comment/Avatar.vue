<template>
  <div class="user">
    <div class="user-img"><img  :src="userImg?userImg:require('./img/icon/avatar.jpg')" :alt="userName"/></div>
  </div>
</template>

<script>
export default {
  props: {
    userId: {
      type: Number,
      default: 1
    },
    userImg: {
      type: String,
      default: ' '
    },
    userName: {
      type: String,
      default: '游客'
    }
  },
  data () {
    return {
    }
  }

}
</script>

<style type="text/css" scoped>
  .user{
    height: 50px;
    width: 50px;
  }
  .user-img img{
    height: 45px;
    width: 45px;
    border-radius:50%;
  }

</style>
