import { createGlobalStyle, css } from 'styled-components'
import reset from 'styled-reset'

type Props = {
  isMobile: boolean
}

const GlobalStyle = createGlobalStyle<Props>`
  ${reset}

  html {
    font-size: 16px;    
  };
  
  body, button {
    font-family: 'kbo-dia';
  }

  body {
    width: 100vw;
    height: 100vh;
  }

   ${(props) =>
     props.isMobile &&
     css`
       /* 가로모드로 고정 */
       @media (orientation: portrait) {
         body {
           transform: rotate(-90deg);
           transform-origin: top left;
           position: absolute;
           top: 100%;
           left: 0;
           width: 100vh;
           height: 100vw;
         }
       }
     `}
  
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
