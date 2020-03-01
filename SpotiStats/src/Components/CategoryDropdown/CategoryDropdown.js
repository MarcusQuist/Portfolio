import React from 'react';
import styles from './CategoryDropdown.module.css';

class CategoryDropdown extends React.Component {
    constructor(props){
      super(props);

      this.state = {
        value: ''
      };
      this.handleChange = this.handleChange.bind(this);
      this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({value: event.target.value});
    }

    handleSubmit() {
        let genre = {
            id: this.state.value,
            genreName: this.state.value
        };
        this.props.onAdd(genre);
        console.log(this.state.value);
    }
    
    render() {
        return (
          <div className={styles.container}>
            <h3>Pick one or more genres to search on</h3>
                <select value={this.state.value} onChange={this.handleChange}>
                    {
                        this.props.genres.map(genre => {
                            return <option value={genre}
                                        key={genre}>
                                {genre}
                            </option>
                        })
                    }
                </select>
            <button className={styles.button} onClick={this.handleSubmit}>Add genre</button>
          </div>
          );
        }
    componentDidUpdate() {
        if(!this.state.value) {
            this.setState({ value: this.props.genres[0] });
          }
    }
    }

export default CategoryDropdown;