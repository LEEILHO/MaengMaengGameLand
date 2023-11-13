'use client'

import { useEffect, useRef } from 'react'

const useDidMountEffect = (func: () => void, deps: React.DependencyList) => {
  const didMount = useRef(false)

  useEffect(() => {
    if (didMount.current) func()
    else didMount.current = true
  }, deps)
}

export default useDidMountEffect
