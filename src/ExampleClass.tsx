import React from 'react';

class ExampleClass extends React.Component<{}, { count: number }> {
  state = {
    count: 0,
  };

  componentDidMount() {
    // 同じコードを2回書く
    document.title = `You clicked ${this.state.count} times`;
  }

  componentDidUpdate() {
    // 同じコードを2回書く
    document.title = `You clicked ${this.state.count} times`;
  }

  render() {
    const { count } = this.state;
    return (
      <div>
        <p>You clicked {count} times</p>
        <button onClick={() => this.setState({ count: count + 1 })}>
          Click me
        </button>
      </div>
    );
  }
}

export default ExampleClass;