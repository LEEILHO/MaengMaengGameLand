import { createGlobalStyle, css } from 'styled-components'
import reset from 'styled-reset'

const GlobalStyle = createGlobalStyle`
  ${reset}

  *{
    box-sizing: border-box;
    /* overscroll-behavior-x: none; */
  }

  * {
    box-sizing: border-box;
    /* 텍스트 터치 막기 */
    -webkit-touch-callout: none; /* iOS Safari */
    -webkit-user-select: none; /* Safari */
    -ms-user-select: none; /* 인터넷익스플로러 */
    user-select: none;
    -webkit-tap-highlight-color:transparent;
  }

  img {
    -webkit-user-drag: none;
    -khtml-user-drag: none;
    -moz-user-drag: none;
    -o-user-drag: none;
  }
  
  body, button, input {
    font-family: 'kbo-dia';
  }

  body {
    overflow: hidden;
    overscroll-behavior: contain;
  }

  html,body {
    width: 100vw;
    height: 100vh;
    width: calc(var(--vw, 1vw) * 100);
    height: calc(var(--vh, 1vh) * 100); 
  }

  body #portal  {
    .modal-overlay {
      width: 100%;
      height: 100%;
      position: fixed;
      top: 0;
      left: 0;
    }
  }

  @font-face {
    font-family: "kbo-dia";
    font-weight: 400;
    src: url("/fonts/KBO-Dia-Gothic_light.woff") format("woff");
    font-style: normal;
  }

  @font-face {
    font-family: "kbo-dia";
    font-weight: 500;
    src: url("/fonts/KBO-Dia-Gothic_medium.woff") format("woff");
    font-style: normal;
  }

  @font-face {
    font-family: "kbo-dia";
    font-weight: 700;
    src: url("/fonts/KBO-Dia-Gothic_bold.woff") format("woff");
    font-style: normal;
  }

  select,
  input,
  button,
  textarea {
    border: 0;
    outline: 0 !important;
  }
`

export default GlobalStyle
