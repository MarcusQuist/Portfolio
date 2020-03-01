import React, { Component } from 'react'
import { Slider, Rail, Handles, Tracks, Ticks } from "react-compound-slider";
import { SliderRail, Handle, Track, Tick } from './renderComponents' // example render components - source below
import styles from './CustomSlider.module.css';

const sliderStyle = {
  position: 'relative',
  width: '100%',
}

const defaultValues = [0, 100]

class CustomSlider extends Component {
  state = {
    domain: [0, 1],
    values: defaultValues.slice(),
    update: defaultValues.slice()
  }

  onChange = values => {
    // new variable assigned from the map which returns implicitly
    const newValues = values.map(value => 
      parseFloat(value.toFixed(2))
    );
    console.log(newValues);

    this.props.onChange(this.props.attribute, newValues);
  }

  setDomain = domain => {
    this.setState({ domain })
  }

  render() {
    const {
      state: { domain, values, update, reversed },
    } = this

    return (
      <div className={styles.Container}>
        <h3>{this.props.attribute}</h3>
        <Slider
          mode={2}
          step={0.01}
          domain={domain}
          reversed={reversed}
          rootStyle={sliderStyle}
          onChange={this.onChange}
          values={values}
        >
          <Rail>
            {({ getRailProps }) => <SliderRail getRailProps={getRailProps} />}
          </Rail>
          <Handles>
            {({ handles, getHandleProps }) => (
              <div className="slider-handles">
                {handles.map(handle => (
                  <Handle
                    key={handle.id}
                    handle={handle}
                    domain={domain}
                    getHandleProps={getHandleProps}
                  />
                ))}
              </div>
            )}
          </Handles>
          <Tracks left={false} right={false}>
            {({ tracks, getTrackProps }) => (
              <div className="slider-tracks">
                {tracks.map(({ id, source, target }) => (
                  <Track
                    key={id}
                    source={source}
                    target={target}
                    getTrackProps={getTrackProps}
                  />
                ))}
              </div>
            )}
          </Tracks>
          <Ticks count={6}>
            {({ ticks }) => (
              <div className="slider-ticks">
                {ticks.map(tick => (
                  <Tick key={tick.id} tick={tick} count={ticks.length} />
                ))}
              </div>
            )}
          </Ticks>
        </Slider>
      </div>
    )
  }
}
export default CustomSlider