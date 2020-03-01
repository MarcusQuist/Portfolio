import React from 'react';
import styles from './SliderCollection.module.css';
import CustomSlider from './CustomSlider';

class SliderCollection extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            Acousticness: [],
            Danceability: [],
            Energy: [],
            Instrumentalness: [],
            Liveness: [],
            Popularity: [],
            Speechiness: [],
            Valence: []
        };
        this.handleChange = this.handleChange.bind(this);
        this.generateUrl = this.generateUrl.bind(this);
    }

    handleChange(name, value){
            this.setState({ [name]: value }, console.log(this.state));
    }

    generateUrl(){
       const url = new URLSearchParams(Object.entries(this.state).reduce((carry, [key, values]) => {
            const [start, end] = values;
        
            if(start > 0 || end < 1) {
                const lowerKey = key.toLowerCase();
                carry[`min_${lowerKey}`] = start;
                carry[`max_${lowerKey}`] = end; 
            }
            return carry;
        
    }, {})).toString()
    console.log(url);
    this.props.setUrl(url);
    this.props.setNotify();
    this.props.setNotifyMessage('Success!', 'Criterias successfully applied.');
}

    render(){
        return(
            <div>
            <div className={styles.Container}>
                <CustomSlider
                attribute='Acousticness'
                onChange={this.handleChange}/>
                <CustomSlider
                attribute='Danceability'
                onChange={this.handleChange}/>
                <CustomSlider
                attribute='Energy'
                onChange={this.handleChange}/>
                <CustomSlider
                attribute='Instrumentalness'
                onChange={this.handleChange}/>
                <CustomSlider
                attribute='Liveness'
                onChange={this.handleChange}/>
                <CustomSlider
                attribute='Popularity'
                onChange={this.handleChange}/>
                <CustomSlider
                attribute='Speechiness'
                onChange={this.handleChange}/>
                <CustomSlider
                attribute='Valence'
                onChange={this.handleChange}/>
            </div>
            <div className={styles.ButtonContainer}>
                <button className={styles.Button}onClick={this.generateUrl}>Apply Criterias</button>
            </div>
            </div>
        )
    }
}

export default SliderCollection;

