import React from 'react'
import { ServerStyleSheet, StyleSheetManager } from 'styled-components'

export function useStyledComponentsRegistry() {
  const [styledComponentsStyleSheet] = React.useState(
    () => new ServerStyleSheet(),
  )

  const styledComponentsFlushEffect = () => {
    const styles = styledComponentsStyleSheet.getStyleElement()
    styledComponentsStyleSheet.instance.clearTag()
    return <>{styles}</>
  }

  const StyledComponentsRegistry = ({
    children,
  }: {
    children: React.ReactNode
  }) => (
    <StyleSheetManager sheet={styledComponentsStyleSheet.instance}>
      {children as React.ReactElement}
    </StyleSheetManager>
  )

  return [StyledComponentsRegistry, styledComponentsFlushEffect] as const
}
