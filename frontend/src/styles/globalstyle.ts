import { createGlobalStyle, css } from 'styled-components'
import reset from 'styled-reset'

type Props = {
  isMobile: boolean
}

const GlobalStyle = createGlobalStyle<Props>`
  ${reset}

  *{
    box-sizing: border-box;
  }
  
  body, button {
    font-family: 'kbo-dia';
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
